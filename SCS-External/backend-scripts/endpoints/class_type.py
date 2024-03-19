import requests as req
try:
    from endpoints.utils import domain, headers
except:
    from utils import domain, headers

# get all class types
def get_class_types_with_approved_status(approved):
    url = f"{domain}/classTypes/approved/{approved}"
    res = req.get(url)
    return res.json()

def change_class_type_approved_status(class_type, approved):
    url = f"{domain}/classTypes/{class_type}/approved/{approved}"
    res = req.put(url, headers=headers)
    return res.json()

print(get_class_types_with_approved_status("true"))
# print(change_class_type_approved_status("swim", "false"))
print(get_class_types_with_approved_status("false"))