import React from 'react';
import githubLogo from '/public/GITHUB.svg';

export default function ContactUs() {
    const groupMembers = [
        { name: "Qasim Li", email: "qasim.li@mail.mcgill.ca", role: "Leader, Developer, Reviewer", github: "https://github.com/lectern", photo: "/public/QL.jpeg" },
        { name: "Henry Huang", email: "feiyang.huang@mail.mcgill.ca", role: "Developer, Reviewer", github: "https://github.com/G1ngerAle", photo: "/public/HENRY.JPG" },
        { name: "Steve Chen", email: "steve.chen@mail.mcgill.ca", role: "Developer, Reviewer", github: "https://github.com/Blacklotus89898", photo: "/public/SC.png" },
        { name: "Bohan Wang", email: "bohan.wang@mail.mcgill.ca", role: "Developer, Reviewer", github: "https://github.com/s026352", photo: "/public/BW.jpg" },
        { name: "Connor Tate", email: "connor.tate@mail.mcgill.ca", role: "Developer, Reviewer", github: "https://github.com/C-Tate", photo: "/public/CT.jpeg" },
        { name: "Anders Woodruff", email: "anders.woodruff@mail.mcgill.ca", role: "Developer, Reviewer", github: "https://github.com/AndersWoodruff", photo: "/public/AW.jpg" },
    ];

    const teamEmail = "info@sportscenter.tech";

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: '1200px', margin: '0 auto', padding: '20px' }}>
            <h1 style={{ textAlign: 'center', margin: '20px 0', fontSize: '2.5em', fontWeight: 'bold' }}>Meet the Team</h1>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
                {groupMembers.map((member, index) => (
                    <div key={index} style={{ padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', textAlign: 'center' }}>
                        <img src={member.photo} alt={member.name} style={{ width: '150px', height: '150px', borderRadius: '50%', display: 'block', margin: '0 auto' }} />
                        <h2>{member.name}</h2>
                        <p>{member.email}</p>
                        <p>{member.role}</p>
                        <a href={member.github} target="_blank" rel="noopener noreferrer">
                            <img src={githubLogo} alt="GitHub" style={{ width: '24px', height: '24px' }} />
                        </a>
                    </div>
                ))}
            </div>
            <section style={{ marginTop: '40px', textAlign: 'center' }}>
                <h2 style={{ fontSize: '2.0em', fontWeight: 'bold' }}>Contact the Team</h2>
                <p>If you have any questions or feedback, feel free to reach out to us at <a href={`mailto:${teamEmail}`}>{teamEmail}</a>.</p>
            </section>
        </div>
    );
}