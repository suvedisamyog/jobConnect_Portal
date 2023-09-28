<?php

require_once '../Data_operations/Org_Operation.php';
$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $jId = isset($_POST['jobId']) ? $_POST['jobId'] : '';

    $db = new Org();
    $userCount = $db->getDetails($jId);

    if ($userCount !== false) {
        $response['error'] = false;
        $response['message'] = $userCount;
    } else {
        $response['error'] = true;
        $response['message'] = "Error retrieving user count.";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
