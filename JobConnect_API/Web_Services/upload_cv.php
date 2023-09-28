<?php

require_once '../Data_operations/User_Operation.php';


$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST"){
    $skill = isset($_POST['skill']) ? $_POST['skill'] : '';
    $education = isset($_POST['education']) ? $_POST['education'] : '';
    $experience = isset($_POST['experience']) ? $_POST['experience'] : '';
    $per_address = isset($_POST['per_address']) ? $_POST['per_address'] : '';
    $temp_address = isset($_POST['temp_address']) ? $_POST['temp_address'] : '';
    $gender = isset($_POST['gender']) ? $_POST['gender'] : '';
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    
 
    if (!empty($skill) && !empty($email) && !empty($per_address) && !empty($temp_address) && !empty($gender)) {  
        $db = new User();

        $userProfile = $db->generateCv($skill, $education, $experience,$per_address, $temp_address,$email,$gender);
         
        if ($userProfile) {
            $response['error'] = false;
            $response['message'] = "cv generated";
                   
        } else {
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    
 
} else {
    $response['error'] = true;
    $response['message'] = "One or more fields are empty. Please fill all the fields and try again.";
}
   

}else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
