const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.addPendingRequestAndNotifyPatient = functions.region('europe-west1').https.onRequest((request, response) => {

 	return admin.firestore().collection("pendingRequests").add({
		ssn: request.query.ssn, 
		hpid: request.query.hpid,
		requestApproved: false,
		requestRejected: false
	}).then((result) => {
		return	admin.firestore().collection("users").where('ssn', '==', request.query.ssn).get().then(snapshot => {
			if (snapshot.empty) {
	 			return response.send("No matching documents");
			}

			var user;
		    snapshot.forEach(doc => {
		    	user = doc;
			});

	//    	var hpname = await admin.firestore().collection

		    const payload = {
				notification: {
			        title: "Allow access to your medical records?",
			        body: "Health care provider " + request.query.hpid + " wants access to your medical records. Approve or reject?",
		    	},
				data: {
		      		requestId: result.id
				}
		    };
		    // Send notifications to all tokens.
		    admin.messaging().sendToDevice(user.data().firebase_token, payload);
			return response.json({result: "Got user from Firebase!" + user.data().firebase_token});
		})
	})
 });

exports.registerRequestApprovedRejected = functions.region('europe-west1').https.onRequest((request, response) => {
	return admin.firestore().collection("pendingRequests").doc(request.query.requestId).get().then(doc => {
		return admin.firestore().collection("healthCareProviders").doc(doc.data().hpid).get().then(hcp => {
			if (request.query.accepted === "true") {
				admin.firestore().collection("pendingRequests").doc(request.query.requestId).update({
					requestApproved: true
				})
			    const payload = {
					notification: {
				        title: "Patient approved your request.",
				        body: "The patient's data can now be accessed."
			    	},
					data: {
			      		requestApprovedId: request.query.requestId
					}
				}
				admin.messaging().sendToDevice(hcp.data().firebase_token, payload);

				return response.json({result: "Notification sent to " + hcp.data().firebase_token});
			}
			else {
				admin.firestore().collection("pendingRequests").doc(request.query.requestId).update({
					requestRejected: true
				})
			    const payload = {
					notification: {
				        title: "Patient did not approve your request.",
				        body: "You are unable to access the patient's data."
			    	},
					data: {
			      		requestApprovedId: request.query.requestId
					}
				}
				admin.messaging().sendToDevice(hcp.data().firebase_token, payload);

				return response.json({result: "Declined Notification sent to " + hcp.data().firebase_token});
			}
		})
	})
 });

