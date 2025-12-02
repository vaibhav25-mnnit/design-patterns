## BUILDER DESIGN PATTERN

- It's a creational design pattern.

- Used to construct object of a class which has many attributes i.e complex object creation.

- If there are many fields in class and we try on creating constructors for each there will be huge number of constructor.


#### Definition:- 
- STEP by STEP object creation. 

- IN java StringBuilder is the example of this design pattern.


#### How to Implement

- In main class constructor pass the builder's object as it's parameter and fill all the optional as well required parameters of main class from this builder's object.

- Each builder has a setter method's for each attribute and the method will return this(i.e the object of builder) and the final object is returned in build method.
- There can be multiple builder's for each class for example student can have CSStudentBuilder and MBAStudentBuilder,etc
- 'Director' will construct a final object by calling a proper builder, also it helps in sequential calling of builder methods, it also call's the build method of builder. 
- Can-NOt handle dynamic request's for object creation but the same is possible in decorator design pattern this is were they differ.