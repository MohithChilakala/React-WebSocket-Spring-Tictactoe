export const getCookie = (cookieName) => {
  const name = cookieName + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookieArray = decodedCookie.split(";");

  for (let i = 0; i < cookieArray.length; i++) {
    let cookie = cookieArray[i].trim();
    if (cookie.indexOf(name) === 0) {
      return cookie.substring(name.length, cookie.length);
    }
  }
  return "";
};

export const setCookie = (cookieName, cookieValue, daysToExpire) => {
  let cookieString = cookieName + "=" + cookieValue;
  if (daysToExpire) {
    const date = new Date();
    date.setTime(date.getTime() + daysToExpire * 24 * 60 * 60 * 1000);
    cookieString += "; expires=" + date.toUTCString();
  }
  document.cookie = cookieString;
};

export const deleteCookie = (cookieName) => {
    const expiredDate = new Date();
    expiredDate.setTime(0);
    document.cookie = cookieName + "=; expires=" + expiredDate.toUTCString();
}