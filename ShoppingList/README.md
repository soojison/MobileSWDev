# ShoppingList

Assignment #2 for AIT-Budapest Spring 2017 Mobile Development course

## Features
* (Google style) splash screen with a custom logo (and AIT text)
* Empty list shows you a sad picture of an empty shopping bag
* Persistent Storage using Realm
* User can add to the list items that has
    * category: food, toiletries, education, apparel, entertainment, misc
    * name
    * description
    * estimated price
    * bought status
* ViewHolder displays icons based on the category
* Displays item description and edit item button when clicked on list item
* Add new items in a new activity
* Edit existing items in a new activity
* Remove items by swiping left or right
    * Undo remove of the item by interacting with the SnackBar
* Remove all items by clicking the button on the ToolBar
* Reorder items by drag and drop
* Customize currency in the settings dialog which uses SharedPreference

## Known Bugs that are out of my reach :(
1. When removing items with open description panes, if you re-add the item to
   the list, the first item's description pane opens. An indexing issue caused
   by using the ExpandableLayout
2. Had to use notifyDatasetChanged rather than itemRemoved/itemAdded since the
   re-adding and removing items cause the index of the all subsequent items to
   change
