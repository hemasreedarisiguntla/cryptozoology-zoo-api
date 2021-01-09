The Cryptozoology Zoo API
You have been hired by The Menagerie of Cryptozoological Creatures. The management wants to track the animals electronically and needs you to build a web API to help facilitate this.

Steps
Create a Spring Web project with H2 as the database. You will need Spring Web, Spring JPA, and H2 as dependencies.
Create a Repository on Github and use this as the remote of the project.
In the Readme, create and document an API specification, based on the stories given below. Remember REST principles when creating these specifications.
Start development.
Profit?
Tips
Let your tests build your code incrementally.
When you get stuck, err on the side of trying an implementation rather than spending time finding the "perfect" solution.
Utilize the tools you have to ease and quicken development, such as MockMVC for testing and Spring Web's built-in processing of requests and responses.
Follow REST and MVC principles to help create loosely coupled solutions and easy to use interfaces.
Commit often with useful messages to allow easily tracking development. Your first commit should always be an empty project that builds.
Stories and Acceptance Criteria
As zookeeper, I want to add animals to my zoo.

Rule: Animal should have a name and a type (flying, swimming, walking)

When I add an animalDto
Then it is in my zoo
As zookeeper, I want to view animals of my zoo.

Given I have added animals to my zoo
When I check my zoo
Then I see all the animals
As a zookeper, I want to feed my animals.

Rule: Animal moods are unhappy or happy. They are unhappy by default.

Given an animal is unhappy
When I give it a treat
Then the animal is happy

Given an animal is happy
When I give it a treat
Then the animal is still happy
As a zookeeper, I want to maintain different types of habitats so that I can put different types of animals in them.

Given I have an empty <habitat>
When I put animal of <type> into a compatible habitat
Then the animal is in the habitat

Given I have an empty <habitat>
When I put animal of <type> into an incompatible habitat
Then the animal habitat should not change
And the animal becomes unhappy

Given I have an occuppied habitat
When I put an animal into the occupied habitat
Then the animal habitat should not change

|   type  |  habitat  |
| --------- | --------- | 
| flying     |   nest    | 
| swimming  |   ocean   | 
| walking   |   forest  | 

As a zookeeper, I want to search zoo data so that I can make reports on my zoo.

Given I have animals in my zoo
When I search for <mood> and <type>
Then I see a list of animals matching only <mood> and <type>

Given I have habitats in my zoo
When I search for empty habitats
Then I see a list of empty habitats