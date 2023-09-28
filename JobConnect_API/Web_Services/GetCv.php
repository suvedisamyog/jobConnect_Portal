<?php

require_once '../Data_operations/User_Operation.php';


$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST"){
   
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    
 
    if (!empty($email)) {  
        $db = new User();

        $data  = $db->getCv($email);
         
        if (!empty($data)) {
            $response['error'] = false;
            $response['data'] = $data;
        } else {
            $response['error'] = true;
        }
    
 
} else {
    $response['error'] = true;
    $response['message'] = "Email not found";
}
   

}else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
