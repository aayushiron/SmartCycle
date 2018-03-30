# Clarifai Android Starter

This is a simple project showing how to use the Clarifai API in Android. It uses the [Clarifai Java Client] to perform Concept recognition.

`RecognizeConceptsActivity` contains most of the non-boilerplate code. In particular, `RecognizeConceptsActivity.onImagePicked` makes the API call to Clarifai.

You can also look at `RecognizeConceptsAdapter.onBindViewHolder` to see how we display the information that the API returns to the user.
