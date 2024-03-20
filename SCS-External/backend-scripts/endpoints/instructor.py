import requests as req
try:
    from utils import domain, headers
except:
    from endpoints.utils import domain, headers

def create_instructor(accountId, name, email, password):
    url = f"{domain}/instructors"
    payload = {
        "id": int(accountId),
        "name": name,
        "email": email,
        "password": password,
    }
    res = req.post(url, json=payload, headers=headers)
    return res.json()

def get_all_instructors():
    url = f"{domain}/instructors"
    res = req.get(url, headers=headers)
    return res.json()

def update_instructor(acccountId, name, email, password):
    url = f"{domain}/instructors/{acccountId}"
    payload = {
        "id": int(acccountId),
        "name": name,
        "email": email,
        "password": password,
    }
    res = req.put(url, json=payload, headers=headers)
    return res.json()

def delete_all_instructors():
    url = f"{domain}/instructors"
    res = req.delete(url, headers=headers)

# print(create_instructor(1, "John Doe", "1arostn@gmai.ca", "password"))
# print(create_instructor(1, "John Doe", "1aros2tn@gmai.ca", "password"))
# print(create_instructor(1, "John Doe", "1aro1stn@gmai.ca", "password"))
# login
# print(req.post(f"{domain}/login", json={"email": "1aro1stn@gmai.ca", "password": "password"}).json())

# print(get_all_instructors())
# print(update_instructor(103, "John Doe", "1arostn@gmai.ca", "password"))

# delete_all_instructors()
# print(get_all_instructors())