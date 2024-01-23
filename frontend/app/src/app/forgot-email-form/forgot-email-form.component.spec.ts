import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotEmailFormComponent } from './forgot-email-form.component';

describe('ForgotEmailFormComponent', () => {
  let component: ForgotEmailFormComponent;
  let fixture: ComponentFixture<ForgotEmailFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForgotEmailFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForgotEmailFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
