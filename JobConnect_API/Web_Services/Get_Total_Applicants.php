<?php

require_once '../Data_operations/Org_Operation.php';

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $jId = isset($_POST['j_id']) ? $_POST['j_id'] : '';

    if (empty($jId)) {
        $jId = 0;
    }

    $db = new Org();
    $userCount = $db->countUser($jId);

    $response['error'] = false;
    $response['message'] =  $userCount;
} else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
?>
