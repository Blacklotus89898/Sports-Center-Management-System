import requests as req
import schedule, class_type
try:
    from endpoints.utils import domain, headers
except:
    from utils import domain, headers

def get_specific_class(class_id):
    url = f'{domain}/specificClass/{class_id}'
    response = req.get(url, headers=headers)
    return response.json()

def create_specific_class(classId, className, year, specificClassName, description, date, startTime, hourDuration, maxCapacity, currentCapacity, registrationFee):
    url = f'{domain}/specificClass'
    data = {
        "classId": classId,
        "classType": className,
        "year": year,
        "specificClassName": specificClassName,
        "description": description,
        "date": date,
        "startTime": startTime,
        "hourDuration": hourDuration,
        "maxCapacity": maxCapacity,
        "currentCapacity": currentCapacity,
        "registrationFee": registrationFee
    }
    response = req.post(url, headers=headers, json=data)
    return response.json()

def get_all_specific_classes():
    url = f'{domain}/specificClass'
    response = req.get(url, headers=headers)
    return response.json()

def get_specific_class_by_id(class_id):
    url = f'{domain}/specificClass/{class_id}'
    response = req.get(url, headers=headers)
    return response.json()

# print(schedule.create_schedule(2021))
# print(class_type.create_class_type("class1", "description", "true"))
print(get_all_specific_classes())
# print(create_specific_class(1, "class1", 2021, "specificClass2", "description", "2021-01-01", "12:00", 1, 10, 0, 100))
# print(get_all_specific_classes())
print(get_specific_class_by_id(5353))