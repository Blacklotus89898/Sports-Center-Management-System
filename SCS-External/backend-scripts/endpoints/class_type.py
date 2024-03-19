import requests as req
try:
    from endpoints.utils import domain, headers
except:
    from utils import domain, headers

def create_class_type(class_type, description, is_approved):
    url = f"{domain}/classType"
    data = {
        "className": class_type,
        "description": description,
        "isApproved": is_approved
    }
    res = req.post(url, headers=headers, json=data)
    return res.json()

# get all class types
def get_class_types_with_approved_status(approved):
    url = f"{domain}/classTypes/approved/{approved}"
    res = req.get(url)
    return res.json()

def change_class_type_approved_status(class_type, approved):
    url = f"{domain}/classTypes/{class_type}/approved/{approved}"
    res = req.put(url, headers=headers)
    return res.json()

def update_class_type(class_type, description):
    url = f"{domain}/classTypes/{class_type}"
    data = {
        "description": description
    }
    res = req.put(url, headers=headers, json=data)
    return res.json()

print(get_class_types_with_approved_status("true"))
# print(change_class_type_approved_status("swim", "false"))
print(get_class_types_with_approved_status("false"))
print(create_class_type("swim", "swimming class", "true"))

print(get_class_types_with_approved_status("true"))
print(get_class_types_with_approved_status("false"))

print(update_class_type("swim", "swimming class for kids"))

print(get_class_types_with_approved_status("true"))
print(get_class_types_with_approved_status("false"))