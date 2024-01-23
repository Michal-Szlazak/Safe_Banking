import requests
import json
from concurrent.futures import ThreadPoolExecutor

# Replace the URL and headers with your target endpoint and authentication details
url = "http://localhost:8081/bank/transfer"
headers = {
    "Authorization": "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJWY05pRU9YVXo0NjlNNEJIWWZsT2FxRi1Fb2doVzFucWtvQkJ2V2p5bjE4In0.eyJleHAiOjE3MDUxNTU2MDMsImlhdCI6MTcwNTE1NTMwMywianRpIjoiMmE5ZTYzODEtNDg4Zi00MDMxLWI0OGItMGVjYjU3M2I4NGM4IiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL1NhZmVCYW5rQXBwIiwic3ViIjoiNzMxNjIyNDQtYzNmNC00NjgyLWFkYTEtNjdiZmE1NDgzOGZlIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYWRtaW4tY2xpIiwic2Vzc2lvbl9zdGF0ZSI6IjZhMjc1OTE3LTAzMmQtNGE5YS05MDQ1LWVmYjNhZTI3ODE2YSIsImFjciI6IjEiLCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiNmEyNzU5MTctMDMyZC00YTlhLTkwNDUtZWZiM2FlMjc4MTZhIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiSm9obiBEb2UiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huZG9lQG1haWwuY29tIiwiZ2l2ZW5fbmFtZSI6IkpvaG4iLCJmYW1pbHlfbmFtZSI6IkRvZSIsImVtYWlsIjoiam9obmRvZUBtYWlsLmNvbSJ9.jy3n6urz809k9ACepW0iGDt3Y7UxDX5o6L7aW5AN7S7YwJAXJaX9sjz1qWspv4V96Ch7xrYATU9HR7jUSb_8TBA_FghKeSpNTri2fi6iXaISsYkEd7ECBrZzJb6frXKlIfQgsCbEUAY8KhAFqk8HKzhMbyRElOrK-0bVfFxr8lHMf0xIRJAsc2Lh33LqtdRZNjZDNBm8795Ms0ZZ4iSLz2GbQsPEb8-wG54Kz3k4SxG7rgTzj2dRSVtIp9P75n7lb3nN0p1p4xo6DnsGJaOLwDFEyxlh9BCv1BoKDBgD45rESZvUYvS-FrMFhwLRdW9mx6_3XDmmf8_hki4FiKxgpA",
    "Content-Type": "application/json",
}

# Number of concurrent requests
num_requests = 100

# Example POST data
post_data = {
    "receiverName": "Jane Doe",
    "title": "Payment",
    "senderAccount": "PL12345678901234567890123456",
    "receiverAccount": "PL09876543210987654321098765",
    "amount": 500
}

# Function to make a POST request to the endpoint
def send_post_request(index):
    
    try:
        response = requests.post(url, headers=headers, data=json.dumps(post_data))
        print(f"Request {index} - Status Code: {response.status_code}")
    except Exception as e:
        print(f"Request {index} - Error: {str(e)}")

# Use ThreadPoolExecutor to send concurrent POST requests
with ThreadPoolExecutor(max_workers=num_requests) as executor:
    # Map the send_post_request function to each index in the range
    executor.map(send_post_request, range(num_requests))
