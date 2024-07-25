import axios from 'axios';
import { useEffect, useState, useContext } from 'react';
import { AuthContext } from './context';

export default function AddMembership() {
    const [applications, setApplications] = useState([]);
    const {id, setId} = useContext(AuthContext);


    const fetchApplications = async () => {
        try {
            const response = await axios.get('http://localhost:8080/getPendingMembership');
            setApplications(response.data);
        } catch (error) {
            console.error('There was an error fetching the applications!', error);
        }
    };

    useEffect(() => {
        fetchApplications();
    }, []);

    const onApproveButton = async (mobileNumber) => {
        try {
            await axios.post(`http://localhost:8080/approveMembership`, null, {
                params: {
                    mobileNumber: mobileNumber,
                    memberId:id
                }
            });
    
            fetchApplications();
            setId(id+1);
        } catch (error) {
            console.error('There was an error approving the application!', error);
        }
    };

    return (
        <div>
            <h2>Membership Applications for Approval</h2>
            {applications.length > 0 ? (
                <ul>
                    {applications.map((application, index) => (
                        <li key={index}>
                            <p><strong>Name:</strong> {application.name}</p>
                            <p><strong>Email:</strong> {application.email}</p>
                            <p><strong>Mobile Number:</strong> {application.mobileNumber}</p>
                            <p><strong>Date of Birth:</strong> {application.dateOfBirth}</p>
                            <p><strong>Address:</strong> {application.address}</p>
                            <p><strong>Gender:</strong> {application.gender}</p>
                            <p><strong>Status:</strong> {application.status}</p>
                            <button onClick={() => onApproveButton(application.mobileNumber)}>Approve</button>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No pending applications.</p>
            )}
        </div>
    );
}
