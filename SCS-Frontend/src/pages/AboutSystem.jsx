import React from 'react';

export default function AboutTheSystem() {
    return (
        <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: '1200px', margin: '0 auto', padding: '20px' }}>
            <h1 style={{ textAlign: 'center', margin: '20px 0', fontSize: '2.5em', fontWeight: 'bold' }}>About the System</h1>
            
            <div style={{ textAlign: 'justify', fontSize: '1.2em' }}>
                <img 
                    src="/public/GYM.jpg" 
                    alt="People in the gym" 
                    style={{ width: '40%', float: 'left', marginRight: '20px', borderRadius: '15px', marginBottom: '20px' }}
                />
                Welcome to Sports Center! We are thrilled to offer a variety of dynamic activities that cater to your fitness journey. From high-energy cardio sessions to calming stretching classes and rigorous strength training, there is something for everyone in our community.
                <br /><br />
                Our dedicated team of instructors is the backbone of our center, bringing expertise, passion, and motivational energy to every session. They are committed to inspiring each member, helping to navigate their fitness goals, and providing a supportive and empowering environment.
                <br /><br />
                The heart of our application is its flexibility and user-friendly design, allowing instructors to adapt and propose new types of classes. All new classes undergo a careful review by the center owner, ensuring they meet our high standards of quality and innovation.
                <br /><br />
                Our platform extends beyond just booking classes. It is a portal enabling customers to explore, register, and immerse themselves in the enriching experience we provide. Here, every leap you take towards fitness is a leap with us.
                <br /><br />
                Thank you for choosing Sports Center. We are excited to be a part of your fitness journey and look forward to welcoming you to our family of fitness enthusiasts!
            </div>
        </div>
    );
}
