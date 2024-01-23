import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartialPasswordComponent } from './partial-password.component';

describe('PartialPasswordComponent', () => {
  let component: PartialPasswordComponent;
  let fixture: ComponentFixture<PartialPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PartialPasswordComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PartialPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
