class ScanOption {
    name;
    fieldName;
    startX;
    startY;
    endX;
    endY;
}

class ScanningData {
    imageToScanInBase64;
    scanningOptions = [];
}

const scanningData = new ScanningData();

function onFileToScanUpload() {
    console.log("Uploading file to scan...");
    let fileInput = document.getElementById("documentToScan");

    if (fileInput.files.length !== 0) {
        let file = fileInput.files[0];
        console.log("File size: " + file.size.toString() + " bytes");

        let imageCanvas = document.getElementById("imageCanvas");
        let imageCtx = imageCanvas.getContext("2d");

        let selectionCanvas = document.getElementById("selectionCanvas");
        let selectionCtx = selectionCanvas.getContext("2d");

        Promise.all([
            createImageBitmap(file),
        ]).then(function(images) {
            imageCanvas.style.visibility = 'visible';
            selectionCanvas.style.visibility = 'visible'

            let image = images[0];

            imageCanvas.height = image.height.toString();
            imageCanvas.width = image.width.toString();

            selectionCanvas.height = image.height.toString();
            selectionCanvas.width = image.width.toString();

            imageCtx.drawImage(image, 0, 0);

            selectionCtx.fillRect(0, 0, 100, 100);
        });

    }
}
