import requests as req
import base64
from PIL import Image
from io import BytesIO

def get_image_data(url):
    # Fetch the image
    response = req.get(url)

    # Open the image
    image = Image.open(BytesIO(response.content))

    # Convert the image to base64
    image_data = base64.b64encode(BytesIO(response.content).getvalue()).decode()

    # Remove the "data:image/jpeg;base64," prefix
    image_data = image_data.replace("data:image/jpeg;base64,", "")

    return image_data

def clear_db():
    # Clear the database
    req.delete("http://localhost:8080/teachingInfos")
    req.delete("http://localhost:8080/classRegistrations")
    req.delete("http://localhost:8080/instructors")
    req.delete("http://localhost:8080/customers")
    req.delete("http://localhost:8080/schedules")
    req.delete("http://localhost:8080/schedules/2024/openingHours")
    req.delete("http://localhost:8080/schedules/2024/customHours")
    req.delete("http://localhost:8080/classTypes")
    req.delete("http://localhost:8080/specificClass/year/2024")
    req.delete("http://localhost:8080/paymentMethod")

if __name__ == "__main__":
    clear_db()
    print("Database cleared")

    print("> Creating mandatory db entries")
    print("Creating schedules")
    print(req.post("http://localhost:8080/schedule", json={"year": 2024}).text)

    print("Creating admin account")
    print(req.post("http://localhost:8080/owner", json={"name": "admin", "email": "admin@sportscenter.com", "password": "adminpass"}).text)
    
    print("Creating instructor account")
    alice = req.post("http://localhost:8080/instructors", json={"name": "alice", "email": "alice@sportscenter.com", "password": "alicepass", "image": get_image_data("https://th.bing.com/th/id/R.aa60854516596608076877ca1e70c9dd?rik=tjUQi5OqWygttw&riu=http%3a%2f%2fwww.kentopham.net%2fwp-content%2fuploads%2f2013%2f02%2fKen-Topham-Photographer-2.jpg&ehk=jYC5lF%2f8CywGQGXpIHL48yj2vYUOCfPSpTN303HpF8E%3d&risl=&pid=ImgRaw&r=0")}).json()
    bob = req.post("http://localhost:8080/instructors", json={"name": "bob", "email": "bob@sportscenter.com", "password": "bobpass", "image": get_image_data("https://i.pinimg.com/originals/3a/12/75/3a127594ba03bbde2ad35f71be0568ab.jpg")}).json()

    print("Creating ClassTypes")
    print(req.post("http://localhost:8080/classType", json={"className": "Cardio", "description": "Get your heart pumping and calories burning in our high-energy cardio class.", "isApproved": True, "icon": "🎽"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Stretching", "description": "Unwind, rejuvenate, and enhance flexibility in our soothing stretching class.", "isApproved": True, "icon": "🤸‍♀"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Strength Training", "description": "Build muscle, increase strength, and sculpt your body in our empowering strength training class.", "isApproved": True, "icon": "🏋️"}).text)

    print("Creating classes")
    payload = {
        "classType": "Cardio",
        "specificClassName": "Running",
        "description": "Run, jog, or walk your way to better health in our Running class. Suitable for all fitness levels.",
        "date": "2024-04-11",
        "startTime": "10:00:00",
        "hourDuration": 2,
        "maxCapacity": 50,
        "registrationFee": 15,
        "image": get_image_data("https://th.bing.com/th/id/R.2b896cd9104b766c409fd9a6fd0fb0a7?rik=hAhD7mizY78y5g&riu=http%3a%2f%2fwww.pixelstalk.net%2fwp-content%2fuploads%2f2016%2f07%2fHD-Running-Background.jpg&ehk=MwwuV0BpWFEGhzALwI103Lda5jWNZKoxbFN%2b6VRzPZk%3d&risl=1&pid=ImgRaw&r=0"),
        "year": 2024,
    }
    running_class = req.post("http://localhost:8080/specificClass", json=payload).json()

    payload = {
        "classType": "Strength Training",
        "specificClassName": "Weight Lifting",
        "description": "Build muscle, increase strength, and sculpt your body in our empowering strength training class. Suitable for all fitness levels.",
        "date": "2024-04-13",
        "startTime": "10:00:00",
        "hourDuration": 2,
        "maxCapacity": 50,
        "registrationFee": 15,
        "image": get_image_data("https://cdn8.dissolve.com/p/D187_254_012/D187_254_012_1200.jpg"),
        "year": 2024,
    }
    weight_lifting = req.post("http://localhost:8080/specificClass", json=payload).json()

    print("Creating teaching info")
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": alice["id"], "classId": running_class["classId"]}).text)

    print("> Creating not mandatory db entries")
    
    print("Creating class types")

    print(req.post("http://localhost:8080/classType", json={"className": "Cardio", "description": "Get your heart pumping and calories burning in our high-energy cardio class.", "isApproved": True, "icon": "🎽"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Yoga", "description": "Find your inner peace and improve flexibility in our calming yoga sessions.", "isApproved": True, "icon": "🧘"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Cycling", "description": "Pedal your way to fitness in our energizing indoor cycling class.", "isApproved": True, "icon": "🚴"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Dance", "description": "Move to the beat and have fun in our lively dance fitness class.", "isApproved": True, "icon": "💃"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "HIIT", "description": "High-Intensity Interval Training for those who want a challenging and effective workout.", "isApproved": True, "icon": "⏱️"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Pilates", "description": "Strengthen your core and improve posture with our Pilates class.", "isApproved": True, "icon": "🤸"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Martial Arts", "description": "Learn discipline and self-defense in our martial arts class.", "isApproved": True, "icon": "🥋"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Boxing", "description": "Develop strength, agility, and confidence in our boxing class.", "isApproved": True, "icon": "🥊"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Meditation", "description": "Relax your mind and reduce stress in our guided meditation sessions.", "isApproved": True, "icon": "🕉️"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Kickboxing", "description": "Combine martial arts techniques with heart-pumping cardio in our kickboxing class.", "isApproved": True, "icon": "🥋"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Zumba", "description": "Dance to great music, with great people, and burn a ton of calories without even realizing it.", "isApproved": True, "icon": "🎶"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "CrossFit", "description": "Varied functional movements performed at high intensity to increase strength and conditioning.", "isApproved": True, "icon": "🏋️"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Aqua Aerobics", "description": "Enjoy a low-impact workout in the pool that's easy on your joints.", "isApproved": True, "icon": "🏊"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Barre", "description": "Combine ballet-inspired moves with elements of Pilates, dance, yoga and strength training.", "isApproved": True, "icon": "🩰"}).text)
    print(req.post("http://localhost:8080/classType", json={"className": "Bootcamp", "description": "A high-intensity workout that combines strength training and aerobic exercises.", "isApproved": True, "icon": "🏋️"}).text)

    print("Creating opening hours")
    payload = {
        "openTime": "08:00:00",
        "closeTime": "22:00:00",
        "year": 2024
    }
    for day in ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]:
        payload["dayOfWeek"] = day
        print(req.post("http://localhost:8080/schedules/2024/openingHours", json=payload).text)
    for day in ["SATURDAY", "SUNDAY"]:
        payload["dayOfWeek"] = day
        payload["openTime"] = "10:00:00"
        payload["closeTime"] = "18:00:00"
        print(req.post("http://localhost:8080/schedules/2024/openingHours", json=payload).text)

    print("Creating classes")
    payload = {
        "classType": "",
        "specificClassName": "",
        "description": "",
        "date": "",
        "startTime": "",
        "hourDuration": 1,
        "maxCapacity": 50,
        "registrationFee": 15,
        "image": "",
        "year": 2024,
    }
    payload["classType"] = "Cycling"
    payload["specificClassName"] = "Advanced Cycling Techniques"
    payload["description"] = "Pedal your way to fitness in our energizing indoor cycling class. Suitable for all fitness levels."
    payload["date"] = "2024-04-11"
    payload["startTime"] = "13:00:00"
    payload["image"] = get_image_data("https://th.bing.com/th/id/R.fdebd1bb0547597596880d9261267d22?rik=xy5r2arXfaiRLQ&pid=ImgRaw&r=0")
    cycling = req.post("http://localhost:8080/specificClass", json=payload)

    payload["classType"] = "Cardio"
    payload["specificClassName"] = "Zumba"
    payload["description"] = "Dance to great music, with great people, and burn a ton of calories without even realizing it. Suitable for all fitness levels."
    payload["date"] = "2024-04-12"
    payload["image"] = get_image_data("https://lh6.googleusercontent.com/-_HWOkR8Gipc/Uc0nyl5-kUI/AAAAAAAAEsY/pFK2-YhviB4/s0/Zumba%2BFitness%2BMumbai.jpg")
    zumba = req.post("http://localhost:8080/specificClass", json=payload)
    
    payload["classType"] = "Cardio"
    payload["specificClassName"] = "Hiking"
    payload["description"] = "Seeing the world while enjoying a nice hike."
    payload["date"] = "2024-04-15"
    payload["image"] = get_image_data("https://i.pinimg.com/originals/83/f2/1b/83f21be81fcb8600565cfbbe2b46b9b7.jpg")
    hiking = req.post("http://localhost:8080/specificClass", json=payload)

    payload["classType"] = "Cardio"
    payload["specificClassName"] = "Hoola Hooping"
    payload["description"] = "Spin me round and round."
    payload["date"] = "2024-04-16"
    payload["image"] = get_image_data("https://th.bing.com/th/id/R.9a7539f761dc2fd6987ba5cb3a9aa421?rik=m3OJaZukKYIplQ&riu=http%3a%2f%2fazbigmedia.com%2fwp-content%2fuploads%2f2014%2f06%2fhula-hoop-class.jpg&ehk=jKL8546rULMvjkTdhmSD2%2bc0Akz6LguU9yW4naAL%2fqA%3d&risl=&pid=ImgRaw&r=0")
    hoola_hooping = req.post("http://localhost:8080/specificClass", json=payload)

    payload = {
        "classType": "Dance",
        "specificClassName": "Hip Hop",
        "description": "Dance to the music of this era.",
        "date": "2024-04-18",
        "startTime": "13:00:00",
        "hourDuration": 1.5,
        "maxCapacity": 35,
        "registrationFee": 25,
        "image": get_image_data("https://th.bing.com/th/id/OIP.tbKDO2ti6rAvctRi_RQ78AHaE7?rs=1&pid=ImgDetMain"),
        "year": 2024,
    }
    hip_hop = req.post("http://localhost:8080/specificClass", json=payload).json()

    print("Creating instructors")
    naomi = req.post("http://localhost:8080/instructors", json={"name": "naomi", "email": "naomi@sportscenter.com", "password": "naomipass", "image": get_image_data("https://th.bing.com/th/id/OIP.9qF1U6-yNZFoYfEWPHCWVAHaE7?rs=1&pid=ImgDetMain")}).json()

    print("Creating teaching info")
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": naomi["id"], "classId": cycling.json()["classId"]}).text)
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": naomi["id"], "classId": zumba.json()["classId"]}).text)
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": naomi["id"], "classId": hiking.json()["classId"]}).text)
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": naomi["id"], "classId": hoola_hooping.json()["classId"]}).text)
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": naomi["id"], "classId": hip_hop["classId"]}).text)