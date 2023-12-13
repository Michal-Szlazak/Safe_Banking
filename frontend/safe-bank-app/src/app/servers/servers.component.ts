import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-servers',
  templateUrl: './servers.component.html',
  styleUrl: './servers.component.css'
})
export class ServersComponent implements OnInit {

  allowNewServer = false;
  serverCreationStatus = "No server was created";
  serverName = 'test';
  serverCreated = false;
  servers = ['Testserver', 'Testserver 2']
  // username = '';
  constructor() {
    setTimeout(() => {
      this.allowNewServer = true;
    },2000);
  }

  ngOnInit() {
  }

  onCreateServer() {
    this.serverCreated = true;
    this.servers.push(this.serverName);
    this.serverCreationStatus = "Server was created. Name: " + this.serverName;
  }

  onUpdateServerName(event: any) {
    this.serverName = event.target.value;
  }

  // onAddUser() {
  //   this.username = '';
  // }
}
