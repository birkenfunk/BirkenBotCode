const fs = require("fs");
const SEMVER_REGEX =  /.*path.*\n/gm;

function removeVersionNumber() {
    const data = fs.readFileSync('licenses.json', 'utf-8');
    const newValue = data.replace(SEMVER_REGEX, '');
    fs.writeFileSync('licenses.json', newValue, 'utf-8');
    console.log('Removed license numbers');
}

removeVersionNumber();
