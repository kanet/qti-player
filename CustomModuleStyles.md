Custom styles can be added to css files, linked to assessmentTest or assessmentItem i.e.
```
<assessmentItem identifier="order" title="Grand Prix of Bahrain" adaptive="false" timeDependent="false">
...
   <styleDeclaration>
      <link href="order.css" userAgent=".*"/>
   </styleDeclaration>
...
```

## Order Module ##
Uses **orderInteraction** selector.

Styles:
  * module-layout: **horizontal** | vertical

```
orderInteraction {
   module-layout: vertical;
}
```