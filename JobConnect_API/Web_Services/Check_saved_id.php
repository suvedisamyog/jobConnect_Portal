<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $jobId = $_POST['JId'];
    $Email = $_POST['Email'];
     
    if (isset($Email) && isset($jobId)) {
        $job = new Job();
        $isSaved = $job->checkId($Email, $jobId);
        
        if ($isSaved) {
            $response['isSaved'] = true;
            $response['message'] = "Job is saved";
        } else {
            $response['isSaved'] = false;
            $response['message'] = "Job is not saved";
        }
    } else {
        $response['error'] = false;
        $response['message'] = "Please try again, Error occurred";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Please try again, Error occurred";
}

echo json_encode($response);
