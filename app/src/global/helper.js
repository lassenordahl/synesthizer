
export function isOverCardLimit(str) {
  if (str === undefined) {
    return false;
  }
  return str.length > 30;
}

export function truncateTitle(str) {
  if (str.length > 52) {
    return str.substring(0, 52) + "...";
  } else {
    return str;
  }
}