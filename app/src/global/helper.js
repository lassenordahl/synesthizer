export function isOverCardLimit(str) {
  if (str === undefined) {
    return false;
  }
  return str.length > 30;
}

export function truncateTitle(str, count) {
  if (str.length > count) {
    return str.substring(0, count) + "...";
  } else {
    return str;
  }
}

function addZeroIfSingleDigit(number) {
  if (number < 10) {
    return "0" + number;
  }
  return number;
}

export function convertToSeconds(durationMs) {
  return Math.floor(durationMs / 60000) + ":" + addZeroIfSingleDigit(Math.floor(durationMs % 60000 / 1000));
}

export function capitalizeFirstLetter(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

export function beautifyString(str) {
  let newStr = ""
  let spaceFlag = true;
  for (let i = 0; i < str.length; i++) {
    if (spaceFlag === true) {
      newStr += str.charAt(i).toUpperCase();
      spaceFlag = false;
    } else if (str.charAt(i) === '_') {
      newStr += ' '
      spaceFlag = true;
    } else {
      newStr += str.charAt(i);
    }
  }

  return newStr
}

export function alphaNumArray() {
  return [
    "a",
    "b",
    "c",
    "d",
    "e",
    "f",
    "g",
    "h",
    "i",
    "j",
    "k",
    "l",
    "m",
    "n",
    "o",
    "p",
    "q",
    "r",
    "s",
    "t",
    "u",
    "v",
    "w",
    "x",
    "y",
    "z",
    "0",
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9",
  ];
}