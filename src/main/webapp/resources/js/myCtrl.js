var app = angular.module("myApp", []);
app.controller("myCtrl", function($scope, $http) {
	saveMap = [];
	//alert("document from eclipse");
	$http.get("http://192.168.1.41:8080/getNotes").then(function(resp) {
		var notes = resp.data;
		$scope.notes = notes;
		notes.forEach(function(item, i, arr) {
			saveMap.push(new mapElement(item.id));
		});	
	}).then(function() {
		askUpdate();
	});
	
	function askUpdate() {
		$http.get("http://192.168.1.41:8080/askUpdate").then(function(resp) {
			askUpdate();
			handle(resp.data);
		});	
	}
	
	function handle(dto) {
		var status = dto.status;
		var recievedNote = dto.note;
//		alert('status is ' + status + ', note =' + recievedNote);
		if (status == 'add') {
			add(recievedNote);
		}
		if (status == 'update') {
			var match = _.find($scope.notes, function(elem) {return elem.id == recievedNote.id});
			updateNote(match, recievedNote);
//			alert("note is updated");
		} else if (status == 'delete') {
			deleteNote(recievedNote);
//			alert('note is deleted');		
		}		
	}
	
	function deleteNote(note) {
		$scope.notes = _.filter($scope.notes, function(element) {
			return element.id != note.id;
		});
	}
	
	function updateNote(prev, updated) {
		prev.heading = updated.heading;
		prev.content = updated.content;
	}
	
	
	function Note(id = Math.random(), heading = "sample", content = "text"){
		this.id = id;
		this.heading = heading; 
		this.content = content;
		this.toString = function() {
			return 'note: id=' + id + ', heading=' + heading + ', content=' + content;
		}
		this.update = function(note) {
			this.heading = note.heading;
			this.content = note.content;
		}
	}
	
	function mapElement(id) {
		this.id = id;
		this.saved = true;
		this.toString = function() {
			return 'pair: id=' + this.id + ', saved=' + this.saved;
		}
	}

	$scope.add = function() {
		savePreviousNote();
		sendAddingRequestToServer();
	}
	
	function add(note) {
		savePreviousNote();
		$scope.notes.push(note);
		addToSaveMap(note);
	}
	
	
	function savePreviousNote() {
		var length = $scope.notes.length;
		if (length > 0) {
			setSavedStatus($scope.notes[length - 1], true);
		}
	}
	
	function sendAddingRequestToServer() {
		return $http.get("http://192.168.1.41:8080/addNote");
	}
	
	function sendNoteToServer(note) {
	//сделать эту функцию, потом разобраться с запросом у сервера
// 		$http.post
	}
	
	function addToSaveMap(note) {
		saveMap.push(new mapElement(note.id));
	}
	
	$scope.savebuttonClicked = function(note) {
		sendNoteToServer(note);
		setSavedStatus(note, !isNoteSaved(note));
	}
	
	function sendNoteToServer(note) {
		$http.put("http://192.168.1.41:8080/updateNote", note);
	}
	
	$scope.editClicked = function(note) {
		setSavedStatus(note, !isNoteSaved(note));
	}
	
	$scope.delete = function(note) {
		sendDeletingRequestToServer(note);
	}
	
	function sendDeletingRequestToServer(note) {
		$http.put("http://192.168.1.41:8080/deleteNote", note.id);
	}
	
	$scope.isNoteSaved = function(note) {
		return isNoteSaved(note);
	}
	
	function setSavedStatus(note, status) {
		getPairFromMap(note).saved = status;
	}
	
	function getPairFromMap(note) {
		return _.find(saveMap, function(element) {
			return element.id == note.id;
		});
	}
	
	$scope.getButtonText = function(note) {
		var isSaved = isNoteSaved(note);
		if (!isSaved) 
			return "save"
		else 
			return "modify";
	}
		
	function isNoteSaved(note) {
		return _.find(saveMap, function(element) {
			return element.id == note.id;
		}).saved;
	}
	
});