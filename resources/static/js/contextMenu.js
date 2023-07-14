const contextMenu = $('#context-menu');
const contextMenuItems = $('#context-menu-items');
let itemInContext;
let contextMenuHidden = true;
let contextMenuWidth;
let windowWidth;
let clickCoords;
let clickCoordsX;
let clickCoordsY;
let menuItemsPopulated;

let populateContextMenuFunction;
let onMenuCloseFunction;

function setupContextMenu(elementClass, populateFunction, onCloseFunction) {
    populateContextMenuFunction = populateFunction;
    onMenuCloseFunction = onCloseFunction;
    setupContextMenuEventListener(elementClass);
    setupClickListener();
    setupKeyupListener();
    setupWindowResizeListener();
}

function setupContextMenuEventListener(elementClass) {
    $(document).on('contextmenu', (event) => {
        toggleContextMenuOff();
        itemInContext = $(clickInsideElement(event, elementClass));
        if (itemInContext.length > 0) {
            event.preventDefault();
            contextMenuItems.empty();
            populateContextMenuFunction(itemInContext);
            if (menuItemsPopulated) {
                toggleContextMenuOn();
                setContextMenuPosition(event);
            }
        } else {
            itemInContext = null;
            toggleContextMenuOff();
        }
    })
}

function clickInsideElement(event, className) {
    let element = event.target;
    if (element.classList.contains(className)) {
        return element;
    } else {
        while ((element = element.parentNode)) {
            if (element.classList && element.classList.contains(className)) {
                return element;
            }
        }
    }
    return false;
}

function getMenuItem(text, onClickEvent) {
    let menuItem = $('<li></li>').addClass('context-menu-item');
    let menuLink = $('<a></a>').addClass('context-menu-link').css('cursor', 'pointer').html(text)
        .on('click', onClickEvent);
    menuItem.append(menuLink);
    return menuItem;
}

function generateContextMenuItem(text, onClickEvent) {
    contextMenuItems.append(getMenuItem(text, onClickEvent));
    menuItemsPopulated = true;
}

function generateContextMenuItemWithSubItems(text, subItems) {
    let menuItem = $('<li></li>').addClass('context-menu-link context-menu-submenu-expander').html(text);
    let subList = $('<ul></ul>').addClass('context-menu-submenu');
    menuItem.append(subList);
    let expandIcon = $('<i></i>').addClass('fas fa-angle-right context-menu-expand-icon');
    menuItem.append(expandIcon);
    for (let subItem of subItems) {
        subList.append(getMenuItem(subItem.text, subItem.onClickEvent));
    }
    contextMenuItems.append(menuItem);
    menuItemsPopulated = true;
}

function setupClickListener() {
    $(document).on('click', (event) => {
        let clickedElement = clickInsideElement(event, 'context-menu-link');
        if (clickedElement) {
            event.preventDefault();
        }
        toggleContextMenuOff();
    })
}

function setupKeyupListener() {
    $(document).on('keyup', (event) => {
        if (event.code === 'Escape') {
            toggleContextMenuOff();
        }
    })
}

function setupWindowResizeListener() {
    $(window).on('resize', () => {
        toggleContextMenuOff();
    })
}

function setContextMenuPosition(event) {
    clickCoords = getPosition(event);
    clickCoordsX = clickCoords.x;
    clickCoordsY = clickCoords.y;

    contextMenuWidth = contextMenu.width() + 4;
    windowWidth = $(window).innerWidth();

    if ((windowWidth - clickCoordsX) < contextMenuWidth) {
        contextMenu.css('left', windowWidth - contextMenuWidth + 'px');
    } else {
        contextMenu.css('left', clickCoordsX + 'px');
    }
    contextMenu.css('top', clickCoordsY + 'px');
}

function getPosition(event) {
    let posx = 0;
    let posy = 0;

    if (event.pageX || event.pageY) {
        posx = event.pageX;
        posy = event.pageY;
    } else if (event.clientX || event.clientY) {
        posx = event.clientX + $(document.body).scrollLeft() + $(document.documentElement).scrollLeft();
        posy = event.clientY + $(document.body).scrollTop() + $(document.documentElement).scrollTop();
    }
    return {
        x: posx,
        y: posy
    }
}

function toggleContextMenuOn() {
    if (contextMenuHidden) {
        contextMenuHidden = false;
        contextMenu.addClass('context-menu--active');
    }
}

function toggleContextMenuOff() {
    menuItemsPopulated = false;
    if (!contextMenuHidden) {
        contextMenuHidden = true;
        contextMenu.removeClass('context-menu--active');
    }
    if (onMenuCloseFunction !== undefined) {
        onMenuCloseFunction();
    }
}