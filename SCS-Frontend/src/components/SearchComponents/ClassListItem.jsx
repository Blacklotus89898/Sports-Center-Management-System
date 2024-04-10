import React from "react";

import Modal from "../Modal";
import UserClassBox from "./UserClassBox";

const noImageUrl = 'https://orbis-alliance.com/wp-content/themes/consultix/images/no-image-found-360x260.png';

// returns a badge with a color based on the status
export const StatusBadge = ({ status }) => {
    let color = '';
    switch (status) {
        case 'full':
            color = 'bg-info'; // red for full
            break;
        case 'ended':
            color = 'bg-gray-400'; // gray for ended
            break;
        default:
            // default color or handle other statuses like "x/y capacity"
            color = 'bg-success'; // green for available
    }
    
    return <span className={`w-full text-xs text-base-100 py-1 px-2 rounded-full whitespace-nowrap ${color}`}>{status.toUpperCase()}</span>;
};

//  displays
const ClassListItem = ({
    id,
    imageSrc,
    status,
    name,
    description,
    date,
    time,
    lengthInHrs,
    instructor,
    registrationFee
}) => {
    return (
        <div 
            className="flex flex-col bg-base-100 hover:cursor-pointer"
            onClick={() => document.getElementById("class_list_modal_" + id).showModal()}
        >
            {/* image */}
            {imageSrc && <img 
                className="w-full rounded-lg object-cover aspect-[9/8]"
                src={`data:image/jpeg;base64,${imageSrc}`}
                alt={name} 
            />}
            {!imageSrc && <img 
                className="w-full rounded-lg object-cover aspect-[9/8]"
                src={noImageUrl}
                alt={name} 
            />}
            
            <div className="py-4">

            {/* class name and status */}
            <div className="flex justify-between items-center font-semibold">
                <div className="text-primary-500 overflow-hidden overflow-ellipsis whitespace-nowrap">
                    {name}
                </div>
                <div className="text-right">
                    <StatusBadge status={status} />
                </div>
            </div>

                <div className="py-1" />

                {/* description */}
                <p className="text-primary-500 text-base overflow-hidden overflow-ellipsis whitespace-nowrap max-w-full">{description}</p>

                <div className="py-1" />

                {/* date, time, length, instructor */}
                <div className="text-primary-100 text-sm">
                    <p>${registrationFee}</p>
                    <p>{date} - {time} - {lengthInHrs} {lengthInHrs != 1 ? " hrs" : " hr"}</p>
                    <p>{instructor === "Instructor TBD" ? instructor : "Instructed by " + instructor}</p>
                </div>
            </div>

            <Modal id={"class_list_modal_" + id} width={"w-11/12 max-w-5xl"}>
                {/* {UserClassBox({ id, imageSrc, status, name, description, date, time, lengthInHrs, instructor })} */}
                <UserClassBox id={id} imageSrc={imageSrc} status={status} name={name} description={description} date={date} time={time} lengthInHrs={lengthInHrs} instructor={instructor} registrationFee={registrationFee} />
            </ Modal>
        </div>
    );
};
  
  export default ClassListItem;