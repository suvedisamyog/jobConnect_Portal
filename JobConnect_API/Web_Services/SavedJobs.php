<?php
require_once '../Data_operations/Job_Operation.php';

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
   
    $userEmail = $_POST["Email"];
    
    if (isset($userEmail)) {
        $job = new Job();
        $results=$job->SavedJobs($userEmail);
        if($results){
            $response['error'] = false;
            $response['data'] = $results;
        }else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }
        
       
    } else {
        $response['error'] = true;
        $response['message'] = " User Email not found";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);
?>
