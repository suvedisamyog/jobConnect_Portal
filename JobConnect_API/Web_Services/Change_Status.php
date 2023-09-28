<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 


if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $jobId = $_POST['jobID'];
    $Email = $_POST['email'];
    $Status= $_POST['status'];
     
    if (!empty($jobId) && !empty($Email) && !empty($Status)) {
        $job = new Job();
        $result = $job->statusUpdate($Email, $jobId,$Status);
        
        if ($result) {
            $response['error'] = false;
            $response['message'] = "Updated Status";
        } else {
            $response['error'] = true;
            $response['message'] = "Error while Updated Status ";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "ID  Email not found";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);