export function isOverCardLimit(str) {
  if (str === undefined) {
    return false;
  }
  return str.length > 30;
}