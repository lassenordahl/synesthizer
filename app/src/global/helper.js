
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

