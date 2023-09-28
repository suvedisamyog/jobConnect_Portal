<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $jobId = $_POST['Job_id'];
    $Email = $_POST['email'];
    $Name = $_POST['name'];
     
    if (!empty($jobId) && !empty($Email) && !empty($Name)) {
        $job = new Job();
        if($job->chcekCv($Email)){
            $result = $job->applyJob($Email, $jobId,$Name);
            if ($result) {
                $response['error'] = false;
                $response['message'] = "Applied Successfully";
            } else {
                $response['error'] = true;
                $response['message'] = "Error while Applying! Try Again";
            }
        }else{
            $response['error'] = true;
            $response['message'] = "NOCV";
        }

       
        
       
    } else {
        $response['error'] = true;
        $response['message'] = "ID or Email not found";
    }

  
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);

