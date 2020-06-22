# This document will contain basic intro to Angular
As I wanted to start with Angular, I was aware that I'm not good at remembering all information,  
which I learn through videos, therefore I will write here tips & points, so I can review them more easily.

## Components
Vital block of Angular. It is used to dynamically change html with necessary css definitions.
Every block contains:
* zzzNAMEzz.component.html
* zzzNAMEzz.component.css
* zzzNAMEzz.component.ts
* zzzNAMEzz.component.spec.ts

But they also can be defined inline in *.component.ts, depending on situation.
What we define are selectors, which has to be unique with already existing ones.
Thereafter, It will find corresponding component to selector and will dynamically add it
to html.

To generate component:
* generate by hand
* use CLI npm/angular/ng generate component -Name-

### Values usage - Binding
It's about how to use defined value in angular inside of HTML.
For that we can use:
* interpolation - inside element block
* data-biding - in attributes of element
  * property has - `[name_of_property]="define_angular_property"`
  * event has - `(name_of_event)="methodName"`

Tips:
`
The MDN (Mozilla Developer Network) offers nice lists of all properties and
 events of the element you're interested in. Googling for YOUR_ELEMENT properties
   or YOUR_ELEMENT events  should yield nice results.
`

For input box it would looke like:
`
<input type="text"
class="form-control"
(input)="onUpdateServerName($event)">
`
where:
* *input* - is action in html
* *onUpdateServerName* - method in angular that will be called, every time a letter is given to to input element
* *$event* - param for the previous method and is passing event with necessary data


### Two-way binding
Binding that works from both side
`
<input type="text"
class="form-control"
[(ngModel)]="propertyNameInAngular">
`
and also add
`import { FormsModule } from '@angular/forms';`


### Directives
Are instructions in our DOM.