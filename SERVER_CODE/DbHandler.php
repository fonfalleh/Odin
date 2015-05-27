<?php
/*Database handler class*/
class DbHandler {
    private $insert_query = <<<'EOT'
            INSERT INTO incidents(incident, lat,lng,created_at,name)
			VALUES (?,?, ?,?,?);
EOT;
    private $select_query = <<<'EOT'
            SELECT * FROM incidents WHERE lat = ? AND lng = ?;
EOT;

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function createIncident($incident, $lat, $long, $timestamp, $name)
	{
        $stmt = $this->conn->prepare($this->insert_query);
        $stmt->bind_param("sddss", $incident,$lat,$long,$timestamp,$name);
        $result = $stmt->execute();
        echo $stmt->error;
        $stmt->close();

        if ($result) {
            // incident row created
            $new_task_id = $this->conn->insert_id;
            if ($new_task_id != 0) {
                // incident created successfully
                return $new_task_id;
            } else {
                // incident failed to create
                return NULL;
            }
        } else {
            // incident failed to create
            return NULL;
        }
	}

    public function deleteIncident($incident_id) {
        $stmt = $this->conn->prepare("DELETE FROM incidents WHERE id = ? ");
        $stmt->bind_param("i", $incident_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

	public function getIncidentByLocation($lat,$long)
	{
        $stmt = $this->conn->prepare($this->select_query);
        $stmt->bind_param("dd", $lat,$long);
        $stmt->execute();
        $incidents = $stmt->get_result();
        $stmt->close();
        return $incidents;
	}

    public function getAllIncidents()
    {
        $stmt = $this->conn->prepare("SELECT * FROM incidents;");
        $stmt->execute();
        $incidents = $stmt->get_result();
       	$result = array();
	 // looping through result and preparing tasks array
    while ($incident = $incidents->fetch_assoc()) {
	$tmp = array();
        $tmp["id"]         = $incident["id"];
        $tmp["incident"]   = utf8_encode($incident["incident"]);
        $tmp["lat"]        = $incident["lat"];
        $tmp["lng"]        = $incident["lng"];
        $tmp["created_at"] = $incident["created_at"];
	$tmp["name"]       = utf8_encode($incident["name"]);
        array_push($result, $tmp);
    }
	//error_log(print_r($tmp));
	$stmt->close();
        return $result;
    }

    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
}
?>
