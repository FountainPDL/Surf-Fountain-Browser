#!/bin/bash

echo "Cleaning old package..."

# Move MainActivity to correct package folder
mkdir -p app/src/main/java/com/surffountain2

# Move correct MainActivity
if [ -f app/src/main/java/com/surffountain/browser/MainActivity.java ]; then
  mv app/src/main/java/com/surffountain/browser/MainActivity.java \
     app/src/main/java/com/surffountain2/MainActivity.java
fi

# Delete old empty package folder
rm -rf app/src/main/java/com/surffountain/browser

# Fix package declaration
sed -i 's/package com\.surffountain\.browser;/package com.surffountain2;/g' \
app/src/main/java/com/surffountain2/MainActivity.java

echo "Done. Package fixed to com.surffountain2"
