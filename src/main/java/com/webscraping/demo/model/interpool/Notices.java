package com.webscraping.demo.model.interpool;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notices {
  String date_of_birth;
  List<String> nationalities;
  String forename;
  String name;
  Links _links;
}
