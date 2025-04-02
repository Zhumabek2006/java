cat <<EOF > main.py
from models.user import User

def main():
    user = User("john_doe", "pass123", "john@example.com")
    user.buy_currency("USD", 100, 0.013, "2025-04-01")
    user.sell_currency("USD", 50, 0.015, "2025-04-02")
    transactions = user.get_info_by_date("2025-04-01", "2025-04-02")
    print("Transactions:", transactions)
    print("Email:", user.get_email())
    print("Login:", user.get_login())

if __name__ == "__main__":
    main()
EOF