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

    print("Creating teaching info")
    print(req.post("http://localhost:8080/teachingInfo", json={"accountId": alice["id"], "classId": running_class["classId"]}).text)

    print("> Creating not mandatory db entries")