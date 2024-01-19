from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
import base64

def encrypt_string(secret_key, iv, plaintext):
    backend = default_backend()
    cipher = Cipher(algorithms.AES(secret_key), modes.CBC(iv), backend=backend)
    encryptor = cipher.encryptor()
    padder = padding.PKCS7(128).padder()

    padded_plaintext = padder.update(plaintext.encode()) + padder.finalize()
    ciphertext = encryptor.update(padded_plaintext) + encryptor.finalize()

    return base64.b64encode(ciphertext).decode()

# Example usage
hex_key = "7d3480a02ebe5b24d46dc14c1805677d5f9615b6f57e48a39d7e8f8e6f4cc1a3"
secret_key = bytes.fromhex(hex_key)
hex_iv = '338D3F2BC5A7EE3E19349AD5475EFAE6'  # Hexadecimal string
iv = bytes.fromhex(hex_iv)  # Convert hex string to bytes


plaintext = '1000'

encrypted_string = encrypt_string(secret_key, iv, plaintext)
print(encrypted_string)
