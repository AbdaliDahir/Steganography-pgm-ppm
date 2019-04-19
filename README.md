# What Is Steganography ?
Steganography is defined as "the art of hiding messages inside media files", in other words, it is the way in which we can hide any message (text, image, audio, …) inside any file (.mp3, .wav, .png, …). This helps people to make sure that only those who know about the presence of the message can obtain it. There are many different methods of performing steganography, but the most famous are the LSB (Least Significant Bit) ones. This kind of method modifies the LSB of different bytes with a bit from the message that will be hidden.

# what we will do
We will use the steganography to pass messages discreetly. 
The steganography consists of hiding information in another piece of information. 
We will be interested here in the hiding an image in another image.
The purpose of this project is to develop an "encryption / Decryption" algorithm

# what we need to know
Some things to take into account to be able to implement this technique:
The 2 images (the start image and the one to hide) must have the same size.
The maximum intensity of the image to be cached should be much lower than that of the principal image. (max 8 in intensity).

<h4> Image Format: </h2>
<pre>
<code>
P2
sizeX sizeY
intensityMax
list of values of the image
</code>
</pre>

<img src="https://i.imgur.com/6oBBuxw.png" /> 

# Working Demo

<img src="https://i.imgur.com/Cn0Qr8R.png" />

# License
Image Steganography is licensed under MIT license.
