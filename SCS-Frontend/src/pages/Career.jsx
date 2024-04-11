import React from 'react';

export default function Career() {
    const jobListings = [
        { title: "Yoga Instructor", location: "Montreal", jobId: "YOG-001" },
        { title: "Swimming Coach", location: "Montreal", jobId: "SWI-002" },
        { title: "Personal Trainer", location: "Montreal", jobId: "PT-003" },
        { title: "Basketball Coach", location: "Montreal", jobId: "BB-004" },
        // ... other job listings
    ];

    const applyEmail = "info@sportscenter.tech";

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: '1200px', margin: '0 auto', padding: '20px' }}>
            <h1 style={{ textAlign: 'center', margin: '40px 0', fontSize: '2.5em', fontWeight: 'bold' }}>Career Opportunities</h1>
            
            <div style={{ display: 'flex', justifyContent: 'space-around', flexWrap: 'wrap', alignItems: 'stretch' }}>
                {jobListings.map((job, index) => (
                    <div key={index} style={{
                        background: 'rgba(255, 255, 255, 0.9)', // Slightly transparent white
                        border: '1px solid rgba(0, 0, 0, 0.1)', // Light border
                        borderRadius: '8px',
                        margin: '10px',
                        padding: '20px',
                        flexGrow: '1',
                        flexBasis: 'calc(50% - 20px)',
                        boxSizing: 'border-box',
                        textAlign: 'center',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'space-between', // Space out the content
                    }}>
                        <div>
                            <h2 style={{ fontWeight: 'bold', marginBottom: '10px' }}>{job.title}</h2>
                            <p style={{ margin: '10px 0' }}><strong>Location:</strong> {job.location}</p>
                            <p style={{ margin: '10px 0' }}><strong>Job ID:</strong> {job.jobId}</p>
                        </div>
                        <div>
                            <span style={{
                                background: '#eee', // Light grey background
                                padding: '5px 10px',
                                borderRadius: '15px',
                                display: 'inline-block',
                                marginTop: '10px'
                            }}>Flex Position</span>
                        </div>
                    </div>
                ))}
            </div>

            <section style={{ textAlign: 'center', margin: '40px 0' }}>
                <h2 style={{ fontSize: '2.0em', fontWeight: 'bold' }}>Apply Now</h2>
                <p>If you’re interested in joining our team, send your application to <a href={`mailto:${applyEmail}`} style={{ color: '#0077CC' }}>{applyEmail}</a>.</p>
            </section>
        </div>
    );
}
