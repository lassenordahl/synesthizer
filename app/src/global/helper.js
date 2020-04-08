export function isOverCardLimit(str) {
  console.log(str);
  if (str === undefined) {
    return false;
  }
  return str.length > 30;
}