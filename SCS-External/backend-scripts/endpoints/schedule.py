import requests as req
from endpoints.utils import domain, headers

def create_schedule(year):
    year = int(year)
    url = f"{domain}/schedule/"
    payload = {
        "year": year
    }
    res = req.post(url, json=payload, headers=headers)
    return res.json()

def get_schedules():
    url = f"{domain}/schedules"
    res = req.get(url)
    return res.json()

def get_schedule(year):
    year = int(year)
    url = f"{domain}/schedule/{year}"
    res = req.get(url)
    return res.json()

def delete_schedule(year):
    year = int(year)
    url = f"{domain}/schedule/{year}"
    res = req.delete(url)

def delete_all_schedules():
    url = f"{domain}/schedules"
    res = req.delete(url)