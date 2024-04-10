import React from 'react';

export default function AboutUs() {
    return (
        <div className="about-us-container" style={{ fontFamily: 'Arial, sans-serif' }}>
            <section className="hero-section" style={{ textAlign: 'center', padding: '50px 0', backgroundColor: '#f3f3f3' }}>
                <h1 style={{ fontSize: '2.5em', fontWeight: 'bold', marginBottom: '20px' }}>Our Story</h1>
                <p style={{ fontSize: '1.2em', maxWidth: '600px', margin: 'auto' }}>
                    Our journey began in a classroom, where we, a group of students with diverse talents, were united by a common assignment. Tasked with developing a system collaboratively, we navigated through the process, learning not just about code, but about each other.
                </p>
            </section>

            <section className="learning-section" style={{ padding: '50px 5%' }}>
                <h2 style={{ textAlign: 'center', fontSize: '2em', fontWeight: 'bold', marginBottom: '30px' }}>What We've Learned</h2>
                <p style={{ textAlign: 'center', fontSize: '1.1em', maxWidth: '800px', margin: 'auto', marginBottom: '30px' }}>
                    Initially, the path was fraught with challenges; communication gaps and unclear responsibilities clouded our progress. With a blend of novice and seasoned programmers, we struggled yet eventually found our stride, transforming obstacles into stepping stones towards our goal.
                </p>
            </section>

            <section className="appreciation-section" style={{ backgroundColor: '#fff', padding: '50px 5%' }}>
                <h2 style={{ textAlign: 'center', fontSize: '2em', fontWeight: 'bold', marginBottom: '30px' }}>Thank You for Using Our System</h2>
                <p style={{ textAlign: 'center', fontSize: '1.1em', maxWidth: '800px', margin: 'auto', marginBottom: '30px' }}>
                    Your engagement is the greatest testament to our efforts. Every click, every navigation, is a step with us on our educational journey.
                </p>
            </section>

            <footer style={{ textAlign: 'center', padding: '30px 0', borderTop: '1px solid #ddd' }}>
                <p>© {new Date().getFullYear()} Sports Center System (SCS). All rights reserved.</p>
            </footer>
        </div>
    );
}
