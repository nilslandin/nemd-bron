const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
export const helloWorld = functions.https.onRequest((request, response) => {
//  const original = request.query.text;

 // response.send("Hello from Firebase!" + original);

    admin.firestore().collection('pendingRequeest').add({HpID: "testId", pnr: "1912121212"});

    return response.json({result: `Message with ID: added.`});
});