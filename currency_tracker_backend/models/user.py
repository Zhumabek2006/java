from firebase_config import db
from datetime import datetime

class User:
    def __init__(self, login: str, password: str, email: str, user_id: str = None):
        self.name = login
        self.password = password
        self.email = email
        self.user_id = user_id or db.collection('users').document().id
        
        user_ref = db.collection('users').document(self.user_id)
        user_ref.set({
            'profile': {
                'name': self.name,
                'password': self.password,
                'email': self.email,
                'total_amount': 0,
                'current_currency': {'som': 1}
            }
        })
    
    def update_login(self, new_login: str, new_password: str, new_email: str):
        self.name = new_login
        self.password = new_password
        self.email = new_email
        user_ref = db.collection('users').document(self.user_id)
        user_ref.update({
            'profile.name': self.name,
            'profile.password': self.password,
            'profile.email': self.email
        })
    
    def add_currency(self, currency: str):
        currency_ref = db.collection('users').document(self.user_id).collection('currencies').document(currency)
        currency_ref.set({
            'amount': 0,
            'count': 0,
            'average_exchange_rate': 0,
            'profit': 0
        })
    
    def remove_currency(self, currency: str):
        currency_ref = db.collection('users').document(self.user_id).collection('currencies').document(currency)
        currency_ref.delete()
    
    def update_currency(self, currency: str, amount: float, count: int, average_exchange_rate: float, profit: float):
        currency_ref = db.collection('users').document(self.user_id).collection('currencies').document(currency)
        currency_ref.update({
            'amount': amount,
            'count': count,
            'average_exchange_rate': average_exchange_rate,
            'profit': profit
        })
    
    def buy_currency(self, currency_name: str, amount: int, exchange_rate: float, date: str):
        user_ref = db.collection('users').document(self.user_id)
        currency_ref = user_ref.collection('currencies').document(currency_name)
        
        if not currency_ref.get().exists:
            self.add_currency(currency_name)
        
        currency_data = currency_ref.get().to_dict()
        cost = amount * exchange_rate
        old_total = currency_data['amount'] * currency_data['average_exchange_rate']
        new_total = old_total + cost
        
        currency_ref.update({
            'amount': currency_data['amount'] + amount,
            'count': currency_data['count'] + 1,
            'average_exchange_rate': new_total / (currency_data['amount'] + amount) if (currency_data['amount'] + amount) > 0 else 0
        })
        
        user_ref.update({'profile.total_amount': firestore.Increment(cost)})
        
        transaction = {
            'type': 'buy',
            'currency': currency_name,
            'amount': amount,
            'rate': exchange_rate,
            'date': date,
            'cost': cost
        }
        user_ref.collection('history').add(transaction)
        return transaction
    
    def update_buy_currency(self, transaction_id: str, amount: int, exchange_rate: float, date: str):
        user_ref = db.collection('users').document(self.user_id)
        transaction_ref = user_ref.collection('history').document(transaction_id)
        old_transaction = transaction_ref.get().to_dict()
        
        if old_transaction and old_transaction['type'] == 'buy':
            currency = old_transaction['currency']
            currency_ref = user_ref.collection('currencies').document(currency)
            currency_data = currency_ref.get().to_dict()
            
            old_cost = old_transaction['amount'] * old_transaction['rate']
            old_total = currency_data['amount'] * currency_data['average_exchange_rate']
            currency_ref.update({
                'amount': currency_data['amount'] - old_transaction['amount'],
                'count': currency_data['count'] - 1
            })
            
            new_cost = amount * exchange_rate
            new_total = old_total - old_cost + new_cost
            currency_ref.update({
                'amount': firestore.Increment(amount),
                'count': firestore.Increment(1),
                'average_exchange_rate': new_total / (currency_data['amount'] - old_transaction['amount'] + amount) if (currency_data['amount'] - old_transaction['amount'] + amount) > 0 else 0
            })
            
            user_ref.update({'profile.total_amount': firestore.Increment(new_cost - old_cost)})
            transaction_ref.update({
                'amount': amount,
                'rate': exchange_rate,
                'date': date,
                'cost': new_cost
            })
    
    def remove_buy_currency(self, transaction_id: str):
        user_ref = db.collection('users').document(self.user_id)
        transaction_ref = user_ref.collection('history').document(transaction_id)
        transaction = transaction_ref.get().to_dict()
        
        if transaction and transaction['type'] == 'buy':
            currency = transaction['currency']
            currency_ref = user_ref.collection('currencies').document(currency)
            currency_data = currency_ref.get().to_dict()
            
            old_cost = transaction['cost']
            old_total = currency_data['amount'] * currency_data['average_exchange_rate']
            currency_ref.update({
                'amount': currency_data['amount'] - transaction['amount'],
                'count': currency_data['count'] - 1,
                'average_exchange_rate': (old_total - old_cost) / (currency_data['amount'] - transaction['amount']) if (currency_data['amount'] - transaction['amount']) > 0 else 0
            })
            
            user_ref.update({'profile.total_amount': firestore.Increment(-old_cost)})
            transaction_ref.delete()
    
    def get_buy_currency_info(self, currency_name: str = None):
        user_ref = db.collection('users').document(self.user_id)
        query = user_ref.collection('history').where('type', '==', 'buy')
        if currency_name:
            query = query.where('currency', '==', currency_name)
        return [doc.to_dict() for doc in query.stream()]
    
    def sell_currency(self, currency_name: str, amount: int, exchange_rate: float, date: str):
        user_ref = db.collection('users').document(self.user_id)
        currency_ref = user_ref.collection('currencies').document(currency_name)
        currency_data = currency_ref.get().to_dict()
        
        if currency_data and currency_data['amount'] >= amount:
            revenue = amount * exchange_rate
            profit = revenue - (amount * currency_data['average_exchange_rate'])
            old_total = currency_data['amount'] * currency_data['average_exchange_rate']
            
            currency_ref.update({
                'amount': currency_data['amount'] - amount,
                'count': currency_data['count'] - 1,
                'average_exchange_rate': (old_total - (amount * currency_data['average_exchange_rate'])) / (currency_data['amount'] - amount) if (currency_data['amount'] - amount) > 0 else 0,
                'profit': currency_data['profit'] + profit
            })
            
            user_ref.update({'profile.total_amount': firestore.Increment(-revenue)})
            transaction = {
                'type': 'sell',
                'currency': currency_name,
                'amount': amount,
                'rate': exchange_rate,
                'date': date,
                'revenue': revenue,
                'profit': profit
            }
            user_ref.collection('history').add(transaction)
            user_ref.collection('total_profit').add({'profit': profit, 'date': date})
            return transaction
    
    def update_sell_currency(self, transaction_id: str, amount: int, exchange_rate: float, date: str):
        user_ref = db.collection('users').document(self.user_id)
        transaction_ref = user_ref.collection('history').document(transaction_id)
        old_transaction = transaction_ref.get().to_dict()
        
        if old_transaction and old_transaction['type'] == 'sell':
            currency = old_transaction['currency']
            currency_ref = user_ref.collection('currencies').document(currency)
            currency_data = currency_ref.get().to_dict()
            
            old_revenue = old_transaction['revenue']
            old_profit = old_transaction['profit']
            old_amount = old_transaction['amount']
            old_total = currency_data['amount'] * currency_data['average_exchange_rate']
            currency_ref.update({
                'amount': currency_data['amount'] + old_amount,
                'count': currency_data['count'] + 1,
                'profit': currency_data['profit'] - old_profit
            })
            
            new_revenue = amount * exchange_rate
            new_profit = new_revenue - (amount * currency_data['average_exchange_rate'])
            currency_ref.update({
                'amount': currency_data['amount'] + old_amount - amount,
                'count': currency_data['count'],
                'average_exchange_rate': (old_total - (old_amount * currency_data['average_exchange_rate'])) / (currency_data['amount'] + old_amount - amount) if (currency_data['amount'] + old_amount - amount) > 0 else 0,
                'profit': currency_data['profit'] - old_profit + new_profit
            })
            
            user_ref.update({'profile.total_amount': firestore.Increment(old_revenue - new_revenue)})
            transaction_ref.update({
                'amount': amount,
                'rate': exchange_rate,
                'date': date,
                'revenue': new_revenue,
                'profit': new_profit
            })
            profit_docs = user_ref.collection('total_profit').where('date', '==', old_transaction['date']).stream()
            for doc in profit_docs:
                doc.reference.update({'profit': new_profit, 'date': date})
                break
    
    def remove_sell_currency(self, transaction_id: str):
        user_ref = db.collection('users').document(self.user_id)
        transaction_ref = user_ref.collection('history').document(transaction_id)
        transaction = transaction_ref.get().to_dict()
        
        if transaction and transaction['type'] == 'sell':
            currency = transaction['currency']
            currency_ref = user_ref.collection('currencies').document(currency)
            currency_data = currency_ref.get().to_dict()
            
            revenue = transaction['revenue']
            profit = transaction['profit']
            old_total = currency_data['amount'] * currency_data['average_exchange_rate']
            currency_ref.update({
                'amount': currency_data['amount'] + transaction['amount'],
                'count': currency_data['count'] + 1,
                'average_exchange_rate': (old_total + (transaction['amount'] * currency_data['average_exchange_rate'])) / (currency_data['amount'] + transaction['amount']) if (currency_data['amount'] + transaction['amount']) > 0 else 0,
                'profit': currency_data['profit'] - profit
            })
            
            user_ref.update({'profile.total_amount': firestore.Increment(revenue)})
            transaction_ref.delete()
            profit_docs = user_ref.collection('total_profit').where('date', '==', transaction['date']).stream()
            for doc in profit_docs:
                doc.reference.delete()
                break
    
    def get_sell_currency_info(self, currency_name: str = None):
        user_ref = db.collection('users').document(self.user_id)
        query = user_ref.collection('history').where('type', '==', 'sell')
        if currency_name:
            query = query.where('currency', '==', currency_name)
        return [doc.to_dict() for doc in query.stream()]
    
    def get_email(self):
        user_ref = db.collection('users').document(self.user_id)
        return user_ref.get().to_dict()['profile']['email']
    
    def get_login(self):
        user_ref = db.collection('users').document(self.user_id)
        return user_ref.get().to_dict()['profile']['name']
    
    def change_current_currency(self, new_currency: str, exchange_rate_to_som: float):
        user_ref = db.collection('users').document(self.user_id)
        current_data = user_ref.get().to_dict()['profile']
        old_currency = list(current_data['current_currency'].keys())[0]
        old_rate = current_data['current_currency'][old_currency]
        
        new_total_amount = (current_data['total_amount'] / old_rate) * exchange_rate_to_som
        user_ref.update({
            'profile.total_amount': new_total_amount,
            'profile.current_currency': {new_currency: exchange_rate_to_som}
        })
    
    def clear_history(self):
        user_ref = db.collection('users').document(self.user_id)
        for doc in user_ref.collection('history').stream():
            doc.reference.delete()
        for doc in user_ref.collection('total_profit').stream():
            doc.reference.delete()
        user_ref.update({'profile.total_amount': 0})
        for currency_doc in user_ref.collection('currencies').stream():
            currency_doc.reference.update({
                'amount': 0,
                'count': 0,
                'average_exchange_rate': 0,
                'profit': 0
            })
    
    def get_info_by_date(self, from_date: str, to_date: str):
        user_ref = db.collection('users').document(self.user_id)
        transactions = [doc.to_dict() for doc in user_ref.collection('history').stream() 
                       if from_date <= doc.to_dict()['date'] <= to_date]
        return transactions