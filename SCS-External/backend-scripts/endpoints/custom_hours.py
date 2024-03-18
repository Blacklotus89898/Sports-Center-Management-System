import requests as req
from endpoints.utils import domain, headers

# get all custom hours
def get_custom_hours():
    url = f"{domain}/customHours"
    res = req.get(url)
    return res.json()

# get custom hours for a specific name
def get_custom_hours_by_name(name):
    url = f"{domain}/customHours/{name}"
    res = req.get(url)
    return res.json()

# get custom hours by date
def get_custom_hours_by_date(date):
    url = f"{domain}/customHours/date/{date}"
    res = req.get(url)
    return res.json()

# create custom hours
def create_custom_hours(name, description, date, start, end, year):
    url = f"{domain}/customHours"
    payload = {
        "name": name,
        "description": description,
        "date": date,
        "openTime": start,
        "closeTime": end,
        "schedule": {
            "year": year
        }
    }
    res = req.post(url, json=payload, headers=headers)
    return res.json()

# update custom hours
def update_custom_hours(name, description, date, start, end, year):
    url = f"{domain}/customHours/{name}"
    payload = {
        "description": description,
        "date": date,
        "openTime": start,
        "closeTime": end,
        "schedule": {
            "year": year
        }
    }
    res = req.put(url, json=payload, headers=headers)
    return res.json()

# delete custom hours
def delete_custom_hours(name):
    url = f"{domain}/customHours/{name}"
    res = req.delete(url, headers=headers)

# delete all custom hours
def delete_all_custom_hours():
    url = f"{domain}/customHours"
    res = req.delete(url, headers=headers)
