<?php
require_once '../Data_operations/Job_Operation.php';

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $jobId = $_POST["jobId"];
    $userEmail = $_POST["userEmail"];
    
    if (isset($jobId) && isset($userEmail)) {
        $db = new Job;
        
        $toggleResult = $db->toggleJob($jobId, $userEmail);
        
        if ($toggleResult === "Removed") {
            $response['error'] = false;
            $response['message'] = "Removed";
        } elseif ($toggleResult === "Added") {
            $response['error'] = false;
            $response['message'] = "Added";
        } else {
            $response['error'] = true;
            $response['message'] = "Error while toggling job";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Job ID or User Email not found";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);
?>
