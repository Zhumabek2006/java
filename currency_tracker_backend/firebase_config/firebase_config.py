cat <<EOF > firebase_config/firebase_config.py
import firebase_admin
from firebase_admin import credentials, firestore

# Replace with your actual service account key path
cred = credentials.Certificate("path/to/your/service-account-key.json")
firebase_admin.initialize_app(cred)
db = firestore.client()
EOF