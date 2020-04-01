import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Comment } from '../../models/comment';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {

  comments: Comment[] = [];
  @ViewChild('commentText', {static: false}) comm: ElementRef;
  constructor() { }

  ngOnInit() {
  }

  addcomment(){
    
  }
  


}
