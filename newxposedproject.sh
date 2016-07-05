#!/bin/bash
while [[ $# > 1 ]]
do
key="$1"

case $key in
    -c|--company)
        COMPANY_NAME="$2"
        shift # past argument
    ;;
    -d|--xposed-desc)
        XPOSED_DESC="$2"
        shift # past argument
    ;;
    -s|--sdk-dir)
        SDK_DIR="$2"
        shift # past argument
    ;;
    -r|--root-project)
        ROOT_PROJECT="$2"
        shift # past argument
    ;;
    -h|--hooks-filename)
        HOOKS_FILE="$2"
        shift # past argument
    ;;
    *)
        USAGE=1
    ;;
esac
shift # past argument or value
done

if [ -z "$COMPANY_NAME" -o -z "$HOOKS_FILE" -o -z "$SDK_DIR" -o -z "$ROOT_PROJECT" -o ! -z "$USAGE" ]; then
    echo "Usage: $0 -c your.company.name.here -d 'xposed description' -h HooksFileNameWithNoJavaExtension -r ROOT_PROJECT_NAME -s android/sdk/dir"
    echo "Usage: $0 --company your.company.name.here -xposed-desc 'xposed description' -hooks-filename HooksFileNameWithNoJavaExtension - ROOT_PROJECT_NAME --sdk-dir android/sdk/dir"
    echo "Xposed description is the only optional argument"
    exit
fi

PACKAGE_NAME="$COMPANY_NAME.$ROOT_PROJECT"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cp -r "$DIR/template" "$ROOT_PROJECT" || exit
sed "s%#{SDK_DIR}%$SDK_DIR%" "$DIR/template/local.properties" > "$ROOT_PROJECT/local.properties"
DIRS="$ROOT_PROJECT/app/src/main/java"
for i in `echo $PACKAGE_NAME | tr '.' ' '`; do
    DIRS="$DIRS/$i"
    mkdir "$DIRS"
done
sed "s/#{PACKAGE_NAME}/$PACKAGE_NAME/" "$ROOT_PROJECT/app/src/main/java/template.java" | sed "s/#{HOOKS_FILE}/$HOOKS_FILE/" > "$DIRS/$HOOKS_FILE.java"
rm "$ROOT_PROJECT/app/src/main/java/template.java"

sed "s/#{ROOT_PROJECT}/$ROOT_PROJECT/" "$DIR/template/settings.gradle" > "$ROOT_PROJECT/settings.gradle"
sed "s/#{HOOKS_FILE}/$HOOKS_FILE/" "$DIR/template/app/src/main/assets/xposed_init" | sed "s/#{PACKAGE_NAME}/$PACKAGE_NAME/" > "$ROOT_PROJECT/app/src/main/assets/xposed_init"
sed "s/#{PACKAGE_NAME}/$PACKAGE_NAME/" "$DIR/template/app/build.gradle" > "$ROOT_PROJECT/app/build.gradle"
sed "s/#{XPOSED_DESCRIPTION}/$XPOSED_DESC/" "$DIR/template/app/src/main/AndroidManifest.xml" | sed "s/#{PACKAGE_NAME}/$PACKAGE_NAME/" > "$ROOT_PROJECT/app/src/main/AndroidManifest.xml"
sed "s/#{ROOT_PROJECT}/$ROOT_PROJECT/" "$DIR/template/app/src/main/res/values/strings.xml" > "$ROOT_PROJECT/app/src/main/res/values/strings.xml"
