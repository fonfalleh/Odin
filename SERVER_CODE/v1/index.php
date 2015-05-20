<?php
ini_set('log_errors',1);
ini_set('display_errors',0);
ini_set('error_log','../log/php-errors.log');
/**
 * Created by PhpStorm.
 * User: bjornahlander
 * Date: 15-04-27
 * Time: 00:17
 */
require_once '../include/DbHandler.php';
require '.././libs/Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json; charset=utf-8');

    echo json_encode($response);
}

/**
 * Adding Middle Layer to authenticate every request
 * Checking if the request has valid api key in the 'Authorization' header
 */
function authenticate(\Slim\Route $route) {
    // Getting request headers
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    // Verifying Authorization Header
    if (isset($headers['Authorization'])) {
        $db = new DbHandler();

        // get the api key
        $api_key = $headers['Authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
        }
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Creating new incident in db
 * method POST
 * params - incident, lat, long
 * url - /incidents
 */
$app->post('/incidents', 'authenticate', function() use ($app) {
    // check for required params
    verifyRequiredParams(array('incident','lat','long'));

    $response = array();
    $incident = $app->request->post('incident');
    $lat = $app->request->post('lat');
    $long = $app->request->post('long');

    $db = new DbHandler();

    // creating new incident
    $incident_id = $db->createIncident($incident, $lat,$long);

    if ($incident_id != NULL) {
        $response["error"] = false;
        $response["message"] = "Incident created successfully";
        $response["incident_id"] = $incident_id;
    } else {
        $response["error"] = true;
        $response["message"] = "Failed to create incident. Please try again";
    }
    echoRespnse(201, $response);
});

/**
 * Listing all incidents
 * method GET
 * url /incidents
 */
$app->get('/incidents', 'authenticate', function() {
    $response = array();
    $db = new DbHandler();

    // fetching all incidents
    $result = $db->getAllIncidents();

    $response["error"] = "false";
    $response["incidents"] = $result;
   // array_push($response["incidents"],$result);
	/*
    // looping through result and preparing tasks array
    while ($incident = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["id"] = $incident["id"];
        $tmp["incident"] = $incident["incident"];
        $tmp["lat"] = $incident["lat"];
        $tmp["lng"] = $incident["lng"];
        $tmp["created_at"] = $incident["created_at"];
        array_push($response["incidents"], $tmp);
    }
*/    error_log("Respones:" . $response["error"]);
    echoRespnse(200, $result);
});

/**
 * Deleting incident.
 * method DELETE
 * url /incidents
 */
$app->delete('/incidents/:id', 'authenticate', function($incident_id) use($app) {
    $db = new DbHandler();
    $response = array();
    $result = $db->deleteIncident($incident_id);
    if ($result) {
        // incident deleted successfully
        $response["error"] = false;
        $response["message"] = "Incident deleted succesfully";
    } else {
        // incident failed to delete
        $response["error"] = true;
        $response["message"] = "Incident failed to delete. Please try again!";
    }
    echoRespnse(200, $response);
});

$app->run();

?>

