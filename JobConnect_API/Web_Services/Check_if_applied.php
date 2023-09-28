<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $jobId = $_POST['Job_id'];
    $Email = $_POST['email'];
     
    if (isset($Email) && isset($jobId)) {
        $job = new Job();
        $result = $job->checkApplied($Email, $jobId);
        
        if ($result['exists']) {
            $response['isApplied'] = true;
            $response['message'] = "Job is applied";
            $response['status'] = $result['status'];
        } else {
            $response['isApplied'] = false;
            $response['message'] = "Job is not applied";
        }
    } else {
        $response['error'] = false;
        $response['message'] = "ID or Email not found";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);

