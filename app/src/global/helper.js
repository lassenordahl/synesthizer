
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